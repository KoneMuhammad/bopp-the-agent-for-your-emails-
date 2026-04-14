package com.bopp.bopp.bopp.agent

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepositoy : JpaRepository<Users, Long>
interface UsageRepository : JpaRepository<Usage, Long>
interface OAuthRepository : JpaRepository<OAuthToken, Long>
interface EmailActionRepository : JpaRepository<EmailAction, Long>