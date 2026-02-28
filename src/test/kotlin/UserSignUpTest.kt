import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.springframework.boot.test.context.SpringBootTest
import user.UserCredentials
import kotlin.test.assertEquals

@SpringBootTest
class  UserRepositoryTest {

    @Test
    fun `user emails match emails from OAuth2 connected accounts`() {
        val emailIds =  getEmailIds(emailIdList)

        UserRepository.save(UserCredentials(emailIds))

        val userEmailIds = UserCredentials.emailIdList
        assertNotNull(userEmailIds)

         assertEquals(emailIds, userEmailIds)

    }
}


