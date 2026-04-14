package com.bopp.bopp.bopp.agent

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepositoy : JpaRepository<Users, Long>