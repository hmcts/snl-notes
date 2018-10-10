locals {
  app_full_name = "${var.product}-${var.component}"

  // Specifies the type of environment. var.env is replaced by pipline
  // to i.e. pr-102-snl so then we need just aat used here
  envInUse = "${(var.env == "preview" || var.env == "spreview") ? "aat" : var.env}"

  // Shared Resources
  vaultName = "${var.raw_product}-${local.envInUse}"
  sharedResourceGroup = "${var.raw_product}-shared-infrastructure-${local.envInUse}"
  sharedAspName = "${var.raw_product}-${local.envInUse}"
  sharedAspRg = "${var.raw_product}-shared-infrastructure-${local.envInUse}"
  asp_name = "${(var.env == "preview" || var.env == "spreview") ? "null" : local.sharedAspName}"
  asp_rg = "${(var.env == "preview" || var.env == "spreview") ? "null" : local.sharedAspRg}"
}

resource "azurerm_resource_group" "rg" {
  name     = "${var.product}-${var.env}"
  location = "${var.location}"

  tags = "${merge(var.common_tags,
      map("lastUpdated", "${timestamp()}")
      )}"
}

module "snl-notes" {
  source               = "git@github.com:hmcts/moj-module-webapp"
  product              = "${var.product}-${var.component}"
  location             = "${var.location}"
  env                  = "${var.env}"
  ilbIp                = "${var.ilbIp}"
  is_frontend          = "${var.external_host_name != "" ? "1" : "0"}"
  additional_host_name = "${var.external_host_name != "" ? var.external_host_name : "null"}"
  subscription         = "${var.subscription}"
  appinsights_instrumentation_key = "${var.appinsights_instrumentation_key}"
  asp_rg               = "${local.asp_rg}"
  asp_name             = "${local.asp_name}"
  common_tags          = "${var.common_tags}"

  app_settings = {
    SNL_NOTES_DB_HOST = "${module.postgres-snl-notes.host_name}"
    SNL_NOTES_DB_PORT = "${module.postgres-snl-notes.postgresql_listen_port}"
    SNL_NOTES_DB_NAME = "${module.postgres-snl-notes.postgresql_database}"
    SNL_NOTES_DB_USERNAME = "${module.postgres-snl-notes.user_name}"
    SNL_NOTES_DB_PASSWORD = "${module.postgres-snl-notes.postgresql_password}"
    SNL_NOTES_DB_PARAMS = "?ssl=true"

    ENABLE_DB_MIGRATE_IN_SERVICE = "false"

    SNL_S2S_JWT_SECRET = "${data.azurerm_key_vault_secret.s2s_jwt_secret.value}"
    SNL_FRONTEND_JWT_SECRET = "${data.azurerm_key_vault_secret.frontend_jwt_secret.value}"
  }

}

module "postgres-snl-notes" {
  source              = "git@github.com:hmcts/moj-module-postgres?ref=master"
  product             = "${var.product}-${var.component}"
  env                 = "${var.env}"
  location            = "${var.location}"
  postgresql_user     = "${var.db_user}"
  database_name       = "${var.db_name}"
  postgresql_version  = "10"
  common_tags         = "${var.common_tags}"
}

# region save DB details to Azure Key Vault
module "snl-vault" {
  source = "git@github.com:hmcts/moj-module-key-vault?ref=master"
  name = "snl-notes-${var.env}"
  product = "${var.product}"
  env = "${var.env}"
  tenant_id = "${var.tenant_id}"
  object_id = "${var.jenkins_AAD_objectId}"
  resource_group_name = "${azurerm_resource_group.rg.name}"
  product_group_object_id = "${var.product_group_object_id}"
}

resource "azurerm_key_vault_secret" "POSTGRES-USER" {
  name      = "${var.product}-${var.component}-POSTGRES-USER"
  value     = "${module.postgres-snl-notes.user_name}"
  vault_uri = "${module.snl-vault.key_vault_uri}"
}

resource "azurerm_key_vault_secret" "POSTGRES-PASS" {
  name      = "${var.product}-${var.component}-POSTGRES-PASS"
  value     = "${module.postgres-snl-notes.postgresql_password}"
  vault_uri = "${module.snl-vault.key_vault_uri}"
}

resource "azurerm_key_vault_secret" "POSTGRES_HOST" {
  name      = "${var.product}-${var.component}-POSTGRES-HOST"
  value     = "${module.postgres-snl-notes.host_name}"
  vault_uri = "${module.snl-vault.key_vault_uri}"
}

resource "azurerm_key_vault_secret" "POSTGRES_PORT" {
  name      = "${var.product}-${var.component}-POSTGRES-PORT"
  value     = "${module.postgres-snl-notes.postgresql_listen_port}"
  vault_uri = "${module.snl-vault.key_vault_uri}"
}

resource "azurerm_key_vault_secret" "POSTGRES_DATABASE" {
  name      = "${var.product}-${var.component}-POSTGRES-DATABASE"
  value     = "${module.postgres-snl-notes.postgresql_database}"
  vault_uri = "${module.snl-vault.key_vault_uri}"
}
# endregion

# region shared Azure Key Vault
data "azurerm_key_vault" "snl-shared-vault" {
  name = "${local.vaultName}"
  resource_group_name = "${local.sharedResourceGroup}"
}

data "azurerm_key_vault_secret" "s2s_jwt_secret" {
  name      = "s2s-jwt-secret"
  vault_uri = "${data.azurerm_key_vault.snl-shared-vault.vault_uri}"
}

data "azurerm_key_vault_secret" "frontend_jwt_secret" {
  name      = "frontend-jwt-secret"
  vault_uri = "${data.azurerm_key_vault.snl-shared-vault.vault_uri}"
}
# endregion