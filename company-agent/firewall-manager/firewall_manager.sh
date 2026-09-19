#!/bin/bash

IP="$1"

if [ -z "$IP" ]; then
    echo "Usage: $0 <IP>"
    exit 1
fi

echo "[FIREWALL] Blocking IP: $IP"

sudo nft add table inet edgerunners 2>/dev/null || true
sudo nft add chain inet edgerunners input '{ type filter hook input priority 0; policy accept; }' 2>/dev/null || true
sudo nft add rule inet edgerunners input ip saddr "$IP" drop 2>/dev/null || true

echo "[FIREWALL] IP blocked: $IP"
