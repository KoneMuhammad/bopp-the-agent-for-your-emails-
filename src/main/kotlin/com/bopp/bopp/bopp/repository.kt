package com.bopp.bopp.bopp

import com.bopp.bopp.bopp.entity.EmailAction
import com.bopp.bopp.bopp.entity.OAuthToken
import com.bopp.bopp.bopp.entity.Usage
import com.bopp.bopp.bopp.entity.Users
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<Users, Long>
interface UsageRepository : JpaRepository<Usage, Long>
interface OAuthRepository : JpaRepository<OAuthToken, Long>
interface EmailActionRepository : JpaRepository<EmailAction, Long>

/**
 *
 * how will the  gmail connection be done?
 * how will the payemnts be done
 */