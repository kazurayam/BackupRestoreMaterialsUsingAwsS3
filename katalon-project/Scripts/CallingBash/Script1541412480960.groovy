def p = 'ps aux'.execute() | 'grep root'.execute() | ['awk', '{print $1,$2,$3}'].execute()
p.waitFor()
println p.text