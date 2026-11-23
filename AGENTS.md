# リポジトリのガイドライン

## 言語設定

- ユーザーへの説明や応答は、全て日本語で行うこと。

## プロジェクト概要

- Java / Spring Boot を使用した Web / Batch アプリケーション
- データベース: PostgreSQL
- Docker Compose による開発用サービス構成: `docker/jnotes/compose.yaml`
  - PostgreSQL、Valkey、Nginx を起動
  - PostgreSQL は日本語ロケール対応の Dockerfile を使い、`POSTGRES_PROFILE` で指定した初期化 SQL をマウント
  - Nginx はホスト側の `localhost:8080` にリバースプロキシ
- Gradle のマルチプロジェクト構成
  - モジュール: `projects/module/` 以下にプロジェクトを配置
  - アプリケーション: `projects/application/` 以下にプロジェクトを配置
  - Java 共通ビルド設定: `buildSrc`
  - Checkstyle 設定: `config/checkstyle`

## ビルド・テスト・開発コマンド

リポジトリのルートから同梱の Gradle Wrapper を使用する。

- `./gradlew build`: 全プロジェクトをコンパイルし、検査とテストを実行して成果物を組み立てる。
- `./gradlew test`: 全プロジェクトの JUnit Platform テストを実行する。
- `./gradlew :projects:application:jnotes-web-app:bootRun`: Web アプリをローカルで起動する。
- `./gradlew :projects:application:jnotes-batch-app:bootRun`: バッチアプリをローカルで起動する。
- `./gradlew check`: テストと設定済みの静的解析を含む検証タスクを実行する。

## コーディング規約

- Java のパッケージは `jp.gr.java_conf.stardiopside.jnotes` をルートとする。

## テスト

- JUnit 5 と JUnit Platform を使用する。

## 境界・禁止事項

- `.env*` ファイルを変更・コミットしない。
- データベースのマイグレーションを自動実行しない。
- 本番環境の設定ファイルを変更する場合は必ず確認を求める。
