package user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

//i want the local database to determine the user and give products even if no wifi (ssd)
@Repository
interface UserRepository : JpaRepository<User, Long> {


}


