package com.bopp.bopp.bopp

import com.bopp.bopp.bopp.auth.OAuthToken
import com.bopp.bopp.bopp.user.Users
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<Users, Long>
interface OAuthRepository : JpaRepository<OAuthToken, Long>

