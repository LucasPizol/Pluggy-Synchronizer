#!/usr/bin/env bash
# Reset PostgreSQL database by dropping application tables.
# Uses same env vars as application: POSTGRES_HOST, POSTGRES_PORT, POSTGRES_DB, POSTGRES_USER, POSTGRES_PASSWORD

set -e

POSTGRES_HOST=${POSTGRES_HOST:-localhost}
POSTGRES_PORT=${POSTGRES_PORT:-5432}
POSTGRES_DB=${POSTGRES_DB:-transactions_db}
POSTGRES_USER=${POSTGRES_USER:-postgres}
POSTGRES_PASSWORD=${POSTGRES_PASSWORD:-postgres}

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SQL_FILE="${SCRIPT_DIR}/../src/main/resources/db/reset.sql"

export PGPASSWORD="$POSTGRES_PASSWORD"
psql -h "$POSTGRES_HOST" -p "$POSTGRES_PORT" -U "$POSTGRES_USER" -d "$POSTGRES_DB" -f "$SQL_FILE"
unset PGPASSWORD

echo "Database reset. Run the application (e.g. ./mvnw quarkus:dev) so Hibernate can recreate tables (update)."
