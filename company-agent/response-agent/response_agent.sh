#!/bin/bash

ACTION="$1"
IP="$2"

echo "[RESPONSE AGENT] Action: $ACTION"
echo "[RESPONSE AGENT] Source IP: $IP"

if [ "$ACTION" = "BLOCK" ]; then
    "$(dirname "$0")/../firewall-manager/firewall_manager.sh" "$IP"
else
    echo "[RESPONSE AGENT] No defensive action required"
fi
