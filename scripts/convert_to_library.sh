#!/bin/bash

# Study 모듈을 Application에서 Library로 변환하는 스크립트
# 실행: ./scripts/convert_to_library.sh

PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
STUDY_DIR="$PROJECT_ROOT/study"

echo "=========================================="
echo "Study 모듈 Library 변환 스크립트"
echo "=========================================="
echo ""

# 카운터
total=0
converted=0
skipped=0

echo "1. build.gradle.kts 변환 중..."
echo ""

# 모든 study 모듈의 build.gradle.kts 찾기
for file in $(find "$STUDY_DIR" -name "build.gradle.kts"); do
    total=$((total + 1))
    echo "처리 중: $(dirname "$file" | sed "s|$PROJECT_ROOT/||")"

    # 이미 library인지 확인
    if grep -q "android.library" "$file"; then
        echo "  [SKIP] 이미 library"
        skipped=$((skipped + 1))
        continue
    fi

    # 1. android.application → android.library
    sed -i '' 's/libs\.plugins\.android\.application/libs.plugins.android.library/g' "$file"

    # 2. applicationId 줄 삭제
    sed -i '' '/applicationId = /d' "$file"

    # 3. versionCode 줄 삭제
    sed -i '' '/versionCode = /d' "$file"

    # 4. versionName 줄 삭제
    sed -i '' '/versionName = /d' "$file"

    echo "  [OK] 변환 완료"
    converted=$((converted + 1))
done

echo ""
echo "2. AndroidManifest.xml 변환 중..."
echo ""

# 모든 study 모듈의 AndroidManifest.xml 찾기
for file in $(find "$STUDY_DIR" -path "*/src/main/AndroidManifest.xml"); do
    echo "처리 중: $(dirname "$file" | sed "s|$PROJECT_ROOT/||")"

    # intent-filter 블록 삭제 (LAUNCHER)
    perl -i -0pe 's/<intent-filter>\s*<action android:name="android\.intent\.action\.MAIN" \/>\s*<category android:name="android\.intent\.category\.LAUNCHER" \/>\s*<\/intent-filter>//gs' "$file"

    echo "  [OK] Manifest 변환"
done

echo ""
echo "=========================================="
echo "변환 완료!"
echo "=========================================="
echo "총 모듈: $total"
echo "변환됨: $converted"
echo "스킵됨: $skipped"
