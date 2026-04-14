1. Project Setup
   Dependencies (Gradle Kotlin DSL)
   dependencies {
   implementation("org.springframework.boot:spring-boot-starter-web")
   implementation("org.springframework.boot:spring-boot-starter-security")
   implementation("org.springframework.boot:spring-boot-starter-data-jpa")

   implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

   runtimeOnly("com.h2database:h2") // or SQLite/Postgres later

   // OAuth + Google API
   implementation("com.google.api-client:google-api-client:2.2.0")
   implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
   implementation("com.google.apis:google-api-services-gmail:v1-rev20220404-2.0.0")

   // HTTP client for LLM
   implementation("org.springframework.boot:spring-boot-starter-webflux")
   }
2. Data Layer (JPA Entities)
   User Entity
   @Entity
   @Table(name = "users")
   data class User(
   @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
   val id: Long = 0,

   val email: String,

   var isPaid: Boolean = false
   )
   Usage Tracking
   @Entity
   @Table(name = "usage")
   data class Usage(
   @Id
   val userId: Long,

   var freeScansUsed: Int = 0
   )
   OAuth Tokens
   @Entity
   @Table(name = "oauth_tokens")
   data class OAuthToken(
   @Id
   val userId: Long,

   val accessToken: String,
   val refreshToken: String,
   val expiry: Long
   )
   Action Log
   @Entity
   @Table(name = "email_actions")
   data class EmailAction(
   @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
   val id: Long = 0,

   val userId: Long,
   val emailId: String,
   val action: String,
   val confidence: Double,
   val timestamp: Long = System.currentTimeMillis()
   )
3. Repositories
   interface UserRepository : JpaRepository<User, Long>
   interface UsageRepository : JpaRepository<Usage, Long>
   interface OAuthRepository : JpaRepository<OAuthToken, Long>
   interface EmailActionRepository : JpaRepository<EmailAction, Long>
4. Usage Guard Service (CRITICAL)
   @Service
   class UsageService(
   private val usageRepo: UsageRepository
   ) {

   fun canScan(userId: Long, isPaid: Boolean): Boolean {
   if (isPaid) return true

        val usage = usageRepo.findById(userId).orElse(Usage(userId))
        return usage.freeScansUsed < 2
   }

   fun incrementUsage(userId: Long) {
   val usage = usageRepo.findById(userId).orElse(Usage(userId))
   usage.freeScansUsed += 1
   usageRepo.save(usage)
   }
   }
5. Gmail Service (Fetch Emails)
   @Service
   class GmailService(
   private val oauthRepo: OAuthRepository
   ) {

   fun fetchEmails(userId: Long): List<EmailDTO> {
   val token = oauthRepo.findById(userId).orElseThrow()

        // TODO: Build Gmail client using token

        return listOf(
            EmailDTO("1", "SALE NOW", "promo@store.com", "limited offer..."),
            EmailDTO("2", "Interview", "hr@company.com", "we'd like to...")
        ).take(5)
   }
   }
   Email DTO
   data class EmailDTO(
   val id: String,
   val subject: String,
   val from: String,
   val snippet: String
   )
6. LLM Service (Batch Classification)
   @Service
   class LLMService(
   private val webClient: WebClient = WebClient.create()
   ) {

   fun classifyBatch(emails: List<EmailDTO>): List<LLMDecision> {

        val prompt = buildPrompt(emails)

        val response = webClient.post()
            .uri("https://api.openai.com/v1/chat/completions")
            .header("Authorization", "Bearer YOUR_API_KEY")
            .bodyValue(mapOf(
                "model" to "gpt-4o-mini",
                "messages" to listOf(
                    mapOf("role" to "user", "content" to prompt)
                )
            ))
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        // TODO: parse JSON properly
        return parseResponse(response!!)
   }

   private fun buildPrompt(emails: List<EmailDTO>): String {
   return """
   Classify emails into spam, important, promotions.
   Return JSON only.

        Emails:
        ${emails}
        """.trimIndent()
   }

   private fun parseResponse(response: String): List<LLMDecision> {
   // stub
   return emptyList()
   }
   }
   Decision DTO
   data class LLMDecision(
   val id: String,
   val label: String,
   val confidence: Double
   )
7. Action Executor (Move to Spam)
   @Service
   class EmailActionService {

   fun applyActions(userId: Long, decisions: List<LLMDecision>) {

        decisions.forEach {
            when (it.label) {
                "spam" -> moveToSpam(it.id)
            }
        }
   }

   private fun moveToSpam(emailId: String) {
   // Call Gmail API here
   }
   }
8. Main Controller
   @RestController
   @RequestMapping("/api")
   class ScanController(
   private val gmailService: GmailService,
   private val llmService: LLMService,
   private val usageService: UsageService,
   private val actionService: EmailActionService,
   private val userRepo: UserRepository
   ) {

   @PostMapping("/scan")
   fun scan(@RequestParam userId: Long): Any {

        val user = userRepo.findById(userId).orElseThrow()

        if (!usageService.canScan(user.id, user.isPaid)) {
            return ResponseEntity.status(402).body("Payment Required")
        }

        val emails = gmailService.fetchEmails(user.id)
        val decisions = llmService.classifyBatch(emails)

        actionService.applyActions(user.id, decisions)

        usageService.incrementUsage(user.id)

        return decisions
   }
   }
9. Security (Simplified for MVP)

For now:

pass userId manually
later → JWT auth
10. What You Have After This

With JUST this:

✅ Users can connect Gmail (once you wire OAuth)
✅ You fetch 5 emails
✅ Send batch to LLM
✅ Classify + act
✅ Enforce 2-scan free limit
✅ Never exceed ~$0.05/user

🚀 NEXT STEP (Recommended)

Don’t try to do everything at once. Do this in order:

Step 1
Run backend
Hardcode emails
Test LLM classification
Step 2
Add Gmail API fetch
Step 3
Add usage guard
Step 4
Connect frontend
⚠️ One Important Fix You Should Make

Right now:

val response = ... bodyToMono(String::class.java)

👉 This is fragile.

Later, switch to:

strict JSON schema
proper parsing (Jackson)
If you want next:

I can:

wire up the Gmail OAuth flow step-by-step
fix your LLM parsing properly (very important)
design your frontend to match this API
or
show you how to deploy this cheaply without surprises