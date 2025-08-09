#!/bin/bash

# === 出力ファイル名 ===
outputFile="combined.txt"

# === 対象拡張子 ===
extensions=()
extensions+=("java")
extensions+=("sql")
extensions+=("xml")
extensions+=("yaml")
extensions+=("gradle")
extensions+=("a5er")
extensions+=("jmx")


# === 出力ファイル初期化 ===
echo "以下が最新のソース一覧ですので共有します" > "$outputFile"

# === 各拡張子のファイルを再帰的に処理 ===
for ext in "${extensions[@]}"; do
    while IFS= read -r -d '' file; do
        echo "[追加中] $file"
        {
            echo
            echo "===== $file ====="
            cat "$file"
            echo
        } >> "$outputFile"
    done < <(find . -type f -name "*.${ext}" -print0)
done

echo "完了: $outputFile に全ファイルを結合しました"
