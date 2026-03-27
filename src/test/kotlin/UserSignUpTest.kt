/**
@DataJpaTest

@Import(UserRepository::class)
class UserRepositoryTest {

    @Autowired
    lateinit var userRepository: UserRepository

    //WHY: the email api could return bad data so I have this filter layer

    @Test
    fun `should insert correct emails from Oauth2 providers into db`() {

        val emailIdListFromEmailBackend = listOf("tackofall1@gmail.com","tackofall123@outlook.com")
        val emailIds = getEmailIds(emailIdListFromEmailBackend)

        val singleUser = User(
            id = 0,
            name = "Tacko")

        singleUser.addEmail(UserEmail(email = "tackofall1@gmail.com", user = singleUser))
        singleUser.addEmail(UserEmail(email = "tackofall123@outlook.com", user = singleUser))

        val user = userRepository.save(singleUser)

        
        assertEquals(
            emailIdListFromEmailBackend,
            user.emails
        )

    }

    }**/
//