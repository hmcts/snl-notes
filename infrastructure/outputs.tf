output "frontend_deployment_endpoint" {
  value = "${module.snl-notes.gitendpoint}"
}
output "microserviceName" {
  value = "${local.app_full_name}"
}

