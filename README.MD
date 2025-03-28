# Shift schedule helper


Bot for send messages and notification.

## Stack:
- Java 17
- Spring Boot 3.4 (web, cache)
- Telegram longpolling 7.9.1

## Commands
- /start - Начало работы
- /schedule - Полное расписание курса
- /next - Следующее занятие

## Required environment variables
### Bot:
- BOT_NAME - Name of telegram bot
- BOT_TOKEN - Bots token
### Sheet (Needs for notifications to work)
- SHEET_ID - Id of GoogleSheet 
- API_KEY - Api key of sheet owner
### Optional parameters
- bot.sheetName - List name with notification clients (default: "subs")
- bot.range - Range with notification clients (default: "A1:A10")