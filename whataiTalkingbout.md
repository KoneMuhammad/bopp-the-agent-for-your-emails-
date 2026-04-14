2. Spring Boot Configuration
   application.yml
   spring:
   datasource:
   url: jdbc:postgresql://db.<project>.supabase.co:5432/postgres
   username: postgres
   password: YOUR_PASSWORD

jpa:
hibernate:
ddl-auto: update
show-sql: true
3. Database Schema (Supabase SQL Editor)

Run this directly inside Supabase:

Users
CREATE TABLE users (
id SERIAL PRIMARY KEY,
email TEXT UNIQUE NOT NULL,
is_paid BOOLEAN DEFAULT FALSE,
created_at TIMESTAMP DEFAULT NOW()
);
Usage Tracking (YOUR COST CONTROL)
CREATE TABLE usage (
user_id INT PRIMARY KEY REFERENCES users(id),
free_scans_used INT DEFAULT 0
);
OAuth Tokens (CRITICAL)
CREATE TABLE oauth_tokens (
user_id INT PRIMARY KEY REFERENCES users(id),
access_token TEXT NOT NULL,
refresh_token TEXT NOT NULL,
expiry BIGINT NOT NULL
);
Email Actions Log
CREATE TABLE email_actions (
id SERIAL PRIMARY KEY,
user_id INT,
email_id TEXT,
action TEXT,
confidence FLOAT,
created_at TIMESTAMP DEFAULT NOW()
);
4. JPA Entities (Same as before)

👉 Good news: NO CHANGE NEEDED

Supabase = PostgreSQL → works directly with Spring Data JPA

5. Updated Backend Flow
   Endpoint: /scan
1. User hits /scan
2. Check usage (Supabase DB)
3. If limit exceeded → return 402
4. Fetch 5 emails (Gmail API)
5. Send batch to LLM
6. Apply actions (move to spam)
7. Increment usage
8. Save logs
6. Usage Guard (UNCHANGED BUT CRITICAL)
   if (!user.isPaid && usage.freeScansUsed >= 2) {
   throw PaymentRequiredException()
   }

👉 This is what protects your wallet.

7. Optional: Use Supabase Auth (YOU CAN SKIP FOR NOW)

Supabase can handle:

login
JWT tokens
sessions
BUT for now (recommended):

👉 Keep it simple:

just store users manually
pass userId
Later upgrade:

Use Supabase Auth + JWT validation in Spring Boot

8. Security Upgrade (IMPORTANT LATER)

When you move beyond MVP:

Verify Supabase JWT in backend
Map JWT → userId
Remove manual userId passing
9. Cost Control (Still Applies)

Even with Supabase:

You must enforce:
max 2 scans (free)
max 5 emails per scan
Why:

Supabase is free…

👉 BUT OpenAI is NOT

10. Final Architecture (Supabase Version)
    React Frontend
    ↓
    Spring Boot Backend
    ↓
    Supabase (PostgreSQL)
    ↓
    Gmail API (OAuth)
    ↓
    LLM API (batch classification)
    ⚠️ Common Mistakes (Avoid These)
    ❌ Storing emails in DB

You don’t need to

❌ Skipping usage tracking

= you lose money

❌ Calling LLM before checking limits

= expensive bug

❌ Using large batches in free tier

Stick to 5 emails

🚀 Your Immediate Next Steps
DO THIS IN ORDER:
1. Create Supabase project
2. Run SQL tables
3. Connect Spring Boot to DB
4. Test saving a user
5. Implement /scan endpoint
6. Add usage guard BEFORE LLM
   🧠 Final Insight

With Supabase:

👉 You now have:

real production DB
zero setup infra
free tier safety

Combined with your:

batch LLM
strict usage limits

👉 You’ve built a cost-controlled AI SaaS foundation