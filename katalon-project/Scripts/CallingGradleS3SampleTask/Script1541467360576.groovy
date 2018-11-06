def p = '../gradle-project/gradlew -b ../gradle-project/build.gradle S3Sample'.execute()
p.waitFor()
println p.text
