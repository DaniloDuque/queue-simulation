#!/bin/bash
set -e

echo "=========================================="
echo "Starting Build Process"
echo "=========================================="

echo ""
echo "[1/3] Cleaning build artifacts..."
./gradlew clean
echo "✓ Clean completed"

echo ""
echo "[2/3] Applying code formatting with Spotless..."
./gradlew spotlessApply
echo "✓ Code formatting applied"

echo ""
echo "[3/3] Building project..."
./gradlew build
echo "✓ Build successful"

echo ""
echo "=========================================="
echo "Build Process Completed Successfully"
echo "=========================================="
