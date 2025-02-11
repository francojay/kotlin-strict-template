#!/bin/bash

# Exit on error
set -e

echo "🚀 Starting deployment process..."

# Check if we're on main branch
current_branch=$(git branch --show-current)
if [ "$current_branch" != "main" ]; then
    echo "❌ Must be on main branch to deploy"
    exit 1
fi

# Get the current version from build.gradle.kts
current_version=$(grep 'version = ' build.gradle.kts | cut -d'"' -f2)

# Increment patch version
IFS='.' read -r major minor patch <<< "$current_version"
new_patch=$((patch + 1))
new_version="$major.$minor.$new_patch"

echo "📦 Updating version from $current_version to $new_version"

# Update version in build.gradle.kts
sed -i '' "s/version = \"$current_version\"/version = \"$new_version\"/" build.gradle.kts

# Run tests
echo "🧪 Running tests..."
./gradlew test

# Production build
echo "🏗️ Building for production..."
SPRING_PROFILES_ACTIVE=production ./gradlew productionBuild

# Stage all changes
echo "📝 Staging all changes..."
git add .

# Commit all changes with just version tag
echo "💾 Committing version $new_version..."
git commit -m "v$new_version"

# Push to main
echo "⬆️ Pushing to main branch..."
git push origin main

echo "✅ Deployment completed successfully!" 