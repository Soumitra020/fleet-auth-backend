package com.sixsense.fleetmanagement

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class FleetManagementApplication

fun main(args: Array<String>) {
    runApplication<FleetManagementApplication>(*args)
}
