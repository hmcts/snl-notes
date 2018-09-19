locals {
  app_full_name = "${var.product}-${var.component}"
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
  is_frontend          = false
  subscription         = "${var.subscription}"
  additional_host_name = "${var.external_host_name}"
  appinsights_instrumentation_key = "${var.appinsights_instrumentation_key}"
  common_tags          = "${var.common_tags}"

  app_settings = {
    SNL_NOTES_DB_HOST = "${module.postgres-snl-notes.host_name}"
    SNL_NOTES_DB_PORT = "${module.postgres-snl-notes.postgresql_listen_port}"
    SNL_NOTES_DB_NAME = "${module.postgres-snl-notes.postgresql_database}"
    SNL_NOTES_DB_USERNAME = "${module.postgres-snl-notes.user_name}"
    SNL_NOTES_DB_PASSWORD = "${module.postgres-snl-notes.postgresql_password}"
    SNL_NOTES_DB_PARAMS = "?ssl=true"

    ENABLE_DB_MIGRATE_IN_SERVICE = "false"
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


# endregion
