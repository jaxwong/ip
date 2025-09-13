# GbTheFatBoy

![Java](https://img.shields.io/badge/Java-17-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-17.0.7-blue)
![Gradle](https://img.shields.io/badge/Gradle-7.1.2-green)
![License](https://img.shields.io/badge/License-MIT-yellow)

**GbTheFatBoy** is a desktop task management application that helps you keep track of your tasks efficiently. It features a modern GUI built with JavaFX and supports various task types including todos, deadlines, and events.

![Product Screenshot](docs/Ui.png)

## ✨ Features

### Task Management

- **Todo Tasks**: Create simple tasks without time constraints
- **Deadline Tasks**: Set tasks with specific due dates and times
- **Event Tasks**: Schedule tasks with start and end times
- **Task Status**: Mark tasks as completed or incomplete
- **Task Deletion**: Remove tasks you no longer need

### Search & Organization

- **Find Tasks**: Search for tasks by keyword
- **Date-based Search**: Find tasks by specific dates
- **Task Tagging**: Add tags to tasks for better organization
- **Task Listing**: View all your tasks in an organized list

### User Experience

- **Modern GUI**: Clean and intuitive JavaFX interface
- **Persistent Storage**: Your tasks are automatically saved and loaded
- **Flexible Date Formats**: Support for multiple date and time input formats
- **Error Handling**: Comprehensive error messages for better user guidance

## 🚀 Quick Start

### Prerequisites

- **JDK 17** or later
- **JavaFX 17** (included in the project)

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/jaxwong/ip.git
   cd ip
   ```

2. **Build the project**

   ```bash
   ./gradlew build
   ```

3. **Run the application**

   ```bash
   ./gradlew run
   ```

   Or run the JAR file directly:

   ```bash
   java -jar build/libs/GbTheFatBoy.jar
   ```

## 📖 Usage

### Adding Tasks

#### Todo Tasks

Create a simple task without any time constraints:

Format: `todo TASK`
Examples:
   - `todo Buy groceries`
   - `todo read book`


#### Deadline Tasks

Create a task with a specific deadline:

Format: `deadline TASK /by DATE`
   -  `DATE` is supported in several formats as shown [here](#date-formats)

Examples:
   - `deadline Submit assignment /by 2024-12-25 23:59`
   - `deadline Finish project /by 25/12/2024 6:00PM`


#### Event Tasks

Create a task that spans a specific time period:

Format: `event TASK /from DATE /to DATE`
   -  `DATE` is supported in several formats as shown [here](#date-formats)


Examples:
   - `event Team meeting /from 2024-12-20 2:00PM /to 2024-12-20 4:00PM`
   - `event Conference /from 20/12/2024 9:00AM /to 22/12/2024 5:00PM`

### Managing Tasks

#### List All Tasks

Format: `list`

#### Mark Task as Complete

Format: `mark TASK_NUMBER`

Examples:
   - `mark 1`

#### Mark Task as Incomplete

Format: `unmark TASK_NUMBER`

Example: 
   - `unmark 2`

#### Delete a Task

Format: `delete TASK_NUMBER`

Example: 
   - `delete 1`

### Searching Tasks

#### Find by Keyword

Format: `find KEYWORD`

Example:
   - `find meeting`
   - `find book`

#### Find by Date

Format: `find-date DATE`

Example: 
   - `find-date 2024-12-25`
   - `find-date 12-09-2025`

### Tagging Tasks


Add tags to tasks for better organization

Format: `tag TASK_NUMBER TAG_MESSAGE`

Example:
   - `tag 1 urgent`

### Exiting the Application

```
bye
```

## 📅 Supported Date Formats

The application supports various date and time formats for your convenience:

### Date Formats

- `yyyy-MM-dd` (e.g., 2024-12-25)
- `dd/MM/yyyy` (e.g., 25/12/2024)
- `MM/dd/yyyy` (e.g., 12/25/2024)
- `yyyy/MM/dd` (e.g., 2024/12/25)

### Time Formats

- `HHmm` (e.g., 1800)
- `HH:mm` (e.g., 18:00)
- `h:mma` (e.g., 6:00PM)
- `h:mm a` (e.g., 6:00 PM)
- `ha` (e.g., 6PM)

## 🏗️ Architecture

This project follows a layered architecture pattern:

- **GUI Layer**: JavaFX-based user interface (`gbthefatboy.gui`)
- **Logic Layer**: Core application logic (`gbthefatboy.entry`, `gbthefatboy.command`)
- **Model Layer**: Task representations (`gbthefatboy.task`)
- **Storage Layer**: Data persistence (`gbthefatboy.storage`)
- **Parser Layer**: Input parsing and validation (`gbthefatboy.parser`)

## 🧪 Testing

Run the test suite to ensure everything works correctly:

```bash
./gradlew test
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/gbthefatboy/
│   │   ├── command/          # Command handling
│   │   ├── entry/            # Main application class
│   │   ├── exception/        # Custom exceptions
│   │   ├── gui/              # JavaFX GUI components
│   │   ├── parser/           # Input parsing utilities
│   │   ├── storage/          # Data persistence
│   │   ├── task/             # Task model classes
│   │   └── ui/               # User interface utilities
│   └── resources/
│       ├── images/           # Application images
│       └── view/             # FXML files
└── test/                     # Test files
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 🙏 Acknowledgments

- This project is based on the Duke project template from CS2103/T
- JavaFX for the modern GUI framework
- Gradle for build automation

---

**Made with ❤️ for better task management**