def p = '../gradle-project/gradlew run'.execute()
p.waitFor()
println p.text