plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "teamsky-backend-assignment"

include("study:boot")
include("study:api")
include("study:app")
include("study:domain")
include("study:infra:persistence")
include("study:infra:cache")
