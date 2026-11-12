# user-api

Project Spring Boot mẫu cho học phần **07.1 · Giới thiệu về AI Devin** (B18, B19 · NTQ AI Training). Dựng lại từ project lab B07 của học phần 04 (GitHub Copilot trên IDE), giữ nguyên 4 bug cố ý.

API quản lý người dùng, lưu dữ liệu **trong bộ nhớ** (tắt app là mất dữ liệu). Không cần database.

> Bộ test `UserCrudTest` có **4 test đỏ** ở trạng thái gốc (tag `devin-start`). Đó là 4 bug cố ý của bài.
> **Không sửa file test.** Sửa code ứng dụng.

## 1. Cần có

| Thành phần | Phiên bản | Ghi chú |
|---|---|---|
| JDK | 21 trở lên (đã chạy thử với 21 và 25) | `JAVA_HOME` trỏ tới JDK |
| Maven | không cần cài | Dùng Maven wrapper: `./mvnw` (Git Bash, Linux, Devin) / `.\mvnw.cmd` (PowerShell). Lần đầu chạy, wrapper tải Maven 3.9.9 và thư viện Spring Boot, mất vài phút |
| Spring Boot | 4.1.2 | Khai báo trong `pom.xml` |

## 2. Lệnh

```bash
./mvnw test                                                    # chạy toàn bộ test
./mvnw test -Dtest='UserCrudTest#updateUnknownUser_returns404' # chạy 1 test
./mvnw spring-boot:run                                         # chạy app ở cổng 8080
```

PowerShell: thay `./mvnw` bằng `.\mvnw.cmd`, tham số `-Dtest` đặt trong ngoặc kép: `"-Dtest=UserCrudTest#updateUnknownUser_returns404"`.

## 3. Kết quả ở trạng thái gốc

`./mvnw test` kết thúc bằng `BUILD FAILURE` và dòng:

```
Tests run: 4, Failures: 3, Errors: 1, Skipped: 0
```

| Test | Kết quả gốc | Nguyên nhân (bug cố ý) |
|---|---|---|
| `createUser_returnsCreatedUser` | Failure | `UserController.create` thiếu `@RequestBody` |
| `findByUsername_returnsMatchingUser` | Failure | `UserRepository.findByUsername` so sánh String bằng `==` |
| `updateUnknownUser_returns404` | Error | `UserService.update` gọi `.get()` trên Optional rỗng (NoSuchElementException) |
| `getUser_doesNotExposePassword` | Failure | `password` bị trả ra trong JSON |

Các cảnh báo `Mockito is currently self-attaching…`, `A Java agent has been loaded dynamically` là bình thường.

## 4. API

| Method | Đường dẫn | Việc |
|---|---|---|
| GET | `/api/users` | Danh sách user |
| GET | `/api/users/{id}` | Một user theo id (không có: 404) |
| GET | `/api/users/by-username/{username}` | Một user theo username (không có: 404) |
| POST | `/api/users` | Tạo user (body JSON), trả 201 |
| PUT | `/api/users/{id}` | Sửa `fullName`, `email` (body JSON) |
| DELETE | `/api/users/{id}` | Xóa user, trả 204 |

## 5. Dữ liệu mẫu

3 user mẫu trong `UserRepository` (email `@example.com`, mật khẩu `fake-pass-00x`) là **dữ liệu giả lập**. Không đưa dữ liệu thật của dự án vào repo này hay vào session Devin.

---

# user-api（日本語）

研修モジュール **07.1「AI Devinの紹介」**（B18・B19、NTQ AI Training）用のSpring Bootサンプルです。モジュール04（IDE上のGitHub Copilot）のB07演習プロジェクトをもとに作成し、意図的な4つのバグをそのまま残しています。

ユーザー管理API。データは**メモリ上**に保存します（アプリ停止で消えます）。データベースは不要です。

> 初期状態（タグ `devin-start`）では `UserCrudTest` の **4件のテストがすべて失敗**します。これは演習用の意図的なバグです。
> **テストファイルは変更しないでください。** アプリケーションのコードを修正します。

## 1. 必要なもの

| 項目 | バージョン | 備考 |
|---|---|---|
| JDK | 21以上（21と25で動作確認済み） | `JAVA_HOME` をJDKに設定 |
| Maven | インストール不要 | Maven Wrapperを使用：`./mvnw`（Git Bash・Linux・Devin）／ `.\mvnw.cmd`（PowerShell）。初回はMaven 3.9.9とSpring Bootのライブラリをダウンロードするため数分かかります |
| Spring Boot | 4.1.2 | `pom.xml` に記載 |

## 2. コマンド

```bash
./mvnw test                                                    # 全テストを実行
./mvnw test -Dtest='UserCrudTest#updateUnknownUser_returns404' # 1件だけ実行
./mvnw spring-boot:run                                         # ポート8080でアプリを起動
```

PowerShellでは `./mvnw` を `.\mvnw.cmd` に置き換え、`-Dtest` はダブルクォートで囲みます：`"-Dtest=UserCrudTest#updateUnknownUser_returns404"`。

## 3. 初期状態の結果

`./mvnw test` は `BUILD FAILURE` で終わり、次の行が出ます。

```
Tests run: 4, Failures: 3, Errors: 1, Skipped: 0
```

| テスト | 初期結果 | 原因（意図的なバグ） |
|---|---|---|
| `createUser_returnsCreatedUser` | Failure | `UserController.create` に `@RequestBody` がない |
| `findByUsername_returnsMatchingUser` | Failure | `UserRepository.findByUsername` で文字列を `==` で比較している |
| `updateUnknownUser_returns404` | Error | `UserService.update` が空のOptionalに `.get()` を呼ぶ（NoSuchElementException） |
| `getUser_doesNotExposePassword` | Failure | JSONに `password` が出力される |

`Mockito is currently self-attaching…`、`A Java agent has been loaded dynamically` の警告は問題ありません。

## 4. API

| メソッド | パス | 処理 |
|---|---|---|
| GET | `/api/users` | ユーザー一覧 |
| GET | `/api/users/{id}` | IDで1件取得（なければ404） |
| GET | `/api/users/by-username/{username}` | ユーザー名で1件取得（なければ404） |
| POST | `/api/users` | ユーザー作成（JSON本文）、201を返す |
| PUT | `/api/users/{id}` | `fullName`・`email` を更新（JSON本文） |
| DELETE | `/api/users/{id}` | ユーザー削除、204を返す |

## 5. サンプルデータ

`UserRepository` の3ユーザー（メール `@example.com`、パスワード `fake-pass-00x`）は**ダミーデータ**です。実案件のデータをこのリポジトリやDevinのセッションに入れないでください。
