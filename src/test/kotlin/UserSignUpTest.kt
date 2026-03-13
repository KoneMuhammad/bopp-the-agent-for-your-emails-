import auth.getEmailIds
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import user.User
import user.UserRepository
import kotlin.test.assertEquals


//google how to resolve this,
//bottleneck is the solving
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    lateinit var userRepository: UserRepository

    //WHY: the email api could return bad data so I have this filter layer
    @Test
    fun `should insert correct emails from Oauth2 providers into db`() {

        val emailIdListFromEmailBackend = listOf("machine", "maths")
        val emailIds = getEmailIds(emailIdListFromEmailBackend)

        val user = userRepository.save(User(emailIds))

        assertEquals(
            emailIdListFromEmailBackend,
            user.emails
        )


    }

    }
