# spring-security-cognito-oauth2-jwt

A sample project with Spring Securitty that reads JWT token and utilize method-level security. 
This version of the project has been rewritten from Kotlin to Java
This version understands several user pools (several issuers).
The code has also been also extended to read idToken (id_token) not just access JWT token. The claims from idToken are added into Principal as 'details'
