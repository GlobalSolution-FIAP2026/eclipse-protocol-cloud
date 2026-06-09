#!/bin/bash
# ══════════════════════════════════════════════════════════════
# Eclipse Protocol — Deletar recursos Azure
# ══════════════════════════════════════════════════════════════

RESOURCE_GROUP="rg-eclipse-protocol"

echo "════════════════════════════════════════════"
echo "  Eclipse Protocol — Remoção Azure"
echo "════════════════════════════════════════════"
echo "  Deletar: $RESOURCE_GROUP"
echo ""
read -p "  Digite 'sim' para confirmar: " CONFIRM

[ "$CONFIRM" != "sim" ] && echo "Cancelado." && exit 0

az group delete --name "$RESOURCE_GROUP" --yes --no-wait

echo ""
echo "Remoção iniciada!"
echo ""
echo "  Confirmar remoção:"
echo "  az group show --name $RESOURCE_GROUP"
echo "  az group list --output table"
echo ""
