package edgerunners.security

default allow := true

allow if {
    input.anomaly == false
}

allow if {
    input.anomaly == true
    input.severity != "HIGH"
}

default action := "ALLOW"

action := "BLOCK" if {
    input.anomaly == true
    input.severity == "HIGH"
}
