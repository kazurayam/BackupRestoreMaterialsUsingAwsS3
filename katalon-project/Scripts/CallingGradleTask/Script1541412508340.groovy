def p = '../gradle-project/gradlew -b ../gradle-project/build.gradle run'.execute()
p.waitFor()
println p.text
