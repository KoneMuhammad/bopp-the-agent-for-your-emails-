import auth.getEmailIds
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.springframework.boot.test.context.SpringBootTest
import user.User
import user.UserRepository
import kotlin.test.assertEquals

@SpringBootTest
class  UserRepositoryTest {

    @Test
    fun `user emails match emails from OAuth2 connected accounts`() {
        val emailIdListFromEmailBackend = listOf("tackoFall", "Diallo",)
        val emailIds =  getEmailIds(emailIdListFromEmailBackend)

        val user = UserRepository().save(User(emailIds))

         assertEquals(emailIdListFromEmailBackend, user.emailIdList)

    }
}


