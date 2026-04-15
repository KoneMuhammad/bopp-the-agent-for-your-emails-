package com.bopp.bopp.bopp

import com.bopp.bopp.bopp.findspam.EmailAction
import com.bopp.bopp.bopp.auth.OAuthToken
import com.bopp.bopp.bopp.findspam.Usage
import com.bopp.bopp.bopp.user.Users
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<Users, Long>
interface UsageRepository : JpaRepository<Usage, Long>
interface OAuthRepository : JpaRepository<OAuthToken, Long>
interface EmailActionRepository : JpaRepository<EmailAction, Long>

