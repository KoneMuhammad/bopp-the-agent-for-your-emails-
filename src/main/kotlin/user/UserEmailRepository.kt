package user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserEmailRepository : JpaRepository<UserEmail, Long>