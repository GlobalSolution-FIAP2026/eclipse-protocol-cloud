#!/bin/bash
# ══════════════════════════════════════════════════════════════
# Eclipse Protocol — Script Azure CLI
# Provisionar VM Linux Ubuntu na Azure
# Abrir portas necessárias (22 e 8080)
# Instalar Docker na VM
# Instalar ferramentas (Git, nano, etc)
# ══════════════════════════════════════════════════════════════
# USO: chmod +x azure-setup.sh && ./azure-setup.sh
# ══════════════════════════════════════════════════════════════

set -e
set -o pipefail

RESOURCE_GROUP="rg-eclipse-protocol"
LOCATION="eastus"
VM_NAME="vm-eclipse-protocol"
VM_IMAGE="Ubuntu2204"
VM_SIZE="Standard_B2s"
ADMIN_USER="eclipseadmin"
NSG_NAME="nsg-eclipse-protocol"
PUBLIC_IP_NAME="pip-eclipse-protocol"

echo "════════════════════════════════════════════"
echo "  Eclipse Protocol — Provisionamento Azure"
echo "════════════════════════════════════════════"

# Resource Group
echo ""
echo "Criando Resource Group '$RESOURCE_GROUP'..."
az group create \
  --name "$RESOURCE_GROUP" \
  --location "$LOCATION" \
  --output table

# VM Linux Ubuntu 22.04
echo ""
echo "Criando VM Ubuntu 22.04 ($VM_SIZE)..."
az vm create \
  --resource-group "$RESOURCE_GROUP" \
  --name "$VM_NAME" \
  --image "$VM_IMAGE" \
  --size "$VM_SIZE" \
  --admin-username "$ADMIN_USER" \
  --generate-ssh-keys \
  --public-ip-sku Standard \
  --public-ip-address "$PUBLIC_IP_NAME" \
  --nsg "$NSG_NAME" \
  --output table

VM_IP=$(az vm show \
  --resource-group "$RESOURCE_GROUP" \
  --name "$VM_NAME" \
  --show-details \
  --query publicIps \
  --output tsv)

echo "VM criada! IP: $VM_IP"

# Abrir portas no NSG
echo ""
echo "Abrindo portas no NSG..."

az network nsg rule create \
  --resource-group "$RESOURCE_GROUP" \
  --nsg-name "$NSG_NAME" \
  --name "Allow-SSH" \
  --protocol Tcp --priority 100 \
  --destination-port-range 22 \
  --access Allow --direction Inbound --output table

az network nsg rule create \
  --resource-group "$RESOURCE_GROUP" \
  --nsg-name "$NSG_NAME" \
  --name "Allow-API-8080" \
  --protocol Tcp --priority 200 \
  --destination-port-range 8080 \
  --access Allow --direction Inbound --output table

echo "Portas: 22 (SSH) | 8080 (API + H2 Console + Swagger)"

# Instalar Docker e ferramentas
echo ""
echo "Instalando Docker e ferramentas..."

az vm run-command invoke \
  --resource-group "$RESOURCE_GROUP" \
  --name "$VM_NAME" \
  --command-id RunShellScript \
  --scripts "
    #!/bin/bash
    set -e
    apt-get update -qq
    apt-get install -y -qq \
      apt-transport-https ca-certificates curl gnupg lsb-release \
      git nano vim htop wget unzip net-tools jq python3

    install -m 0755 -d /etc/apt/keyrings
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg \
      | gpg --dearmor -o /etc/apt/keyrings/docker.gpg
    chmod a+r /etc/apt/keyrings/docker.gpg

    echo \
      \"deb [arch=\$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
      https://download.docker.com/linux/ubuntu \$(lsb_release -cs) stable\" \
      | tee /etc/apt/sources.list.d/docker.list > /dev/null

    apt-get update -qq
    apt-get install -y -qq \
      docker-ce docker-ce-cli containerd.io \
      docker-buildx-plugin docker-compose-plugin

    systemctl enable docker && systemctl start docker
    usermod -aG docker $ADMIN_USER

    mkdir -p /opt/eclipse-protocol
    chown $ADMIN_USER:$ADMIN_USER /opt/eclipse-protocol

    docker --version
    docker compose version
    git --version
    echo 'Instalação concluída!'
  " --output table

echo ""
echo "════════════════════════════════════════════"
echo "  Infraestrutura pronta!"
echo "════════════════════════════════════════════"
echo ""
echo "  IP Público : $VM_IP"
echo "  VM         : $VM_NAME ($VM_SIZE)"
echo "  Portas     : 22 (SSH) | 8080 (API)"
echo ""
echo "  ── Próximos passos ─────────────────────────"
echo "  ssh $ADMIN_USER@$VM_IP"
echo "  cd /opt/eclipse-protocol"
echo "  git clone https://github.com/GlobalSolution-FIAP2026/eclipse-protocol-cloud.git ."
echo "  docker compose up -d --build"
echo ""
echo "  API        : http://$VM_IP:8080"
echo "  Swagger    : http://$VM_IP:8080/swagger-ui.html"
echo "  H2 Console : http://$VM_IP:8080/h2-console"
echo ""
echo "  AO FINALIZAR: ./azure-delete.sh"
echo "════════════════════════════════════════════"