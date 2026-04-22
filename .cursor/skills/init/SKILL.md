---
name: init
description: Initializes project-specific Cursor rules for this Spring Boot codebase by scanning the repository structure and generating or updating the root .cursorrules. Use when the user types /init, asks to create .cursorrules, or wants project-specific coding rules for this repo.
---

# /init (Project initialization)

## Goal
Read this Spring Boot repository, identify its structure/conventions, then create or update the root `.cursorrules` so future changes follow the project's patterns.

## Workflow
1. **Scan the tech stack**
   - Read `pom.xml` to capture Spring Boot version, Java version, and key dependencies (JPA, Security, JWT, Thymeleaf, mail, LangChain4j, etc.).
   - Read `src/main/resources/application.yml` to understand configuration style (env vars, key namespaces).
2. **Scan the codebase shape**
   - Identify top-level packages under `src/main/java/**/` (e.g., controller/service/repository/model/dto/form/mapper/security/config/exception/utils/enums).
   - Spot any notable patterns: `service` interface + `service.impl`, use of specifications, global exception handler, JWT filter/utils, mapping approach.
3. **Generate `.cursorrules`**
   - Write concise repo-specific rules (not generic Java advice) covering:
     - Layering boundaries (controller/service/repository)
     - DTO vs Entity exposure
     - Validation and error handling conventions
     - JPA performance pitfalls (N+1, pagination, transactions)
     - Security constraints (no logging tokens/secrets; JWT/Spring Security patterns)
     - Configuration and secret management (env vars; never commit `.env`)
     - Any AI integration constraints if present
4. **Verify**
   - Ensure `.cursorrules` matches the observed packages and stack.
   - Keep it practical and short; no long prose.

## Output requirements
- The resulting `.cursorrules` must be tailored to the repo (mention real packages/folders).
- Do not include secrets or copy `.env` contents into `.cursorrules`.
- If `.cursorrules` already exists, update it rather than replacing with a generic template.

## When not to run
- If the task is unrelated to repository conventions (e.g., purely answering a conceptual question), do not generate or modify `.cursorrules`.
