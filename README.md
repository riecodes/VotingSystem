# Philippine Voting System

A comprehensive Java-based voting system designed for Philippine elections, supporting various electoral positions from national to local levels. This system provides a secure and user-friendly interface for managing candidates, recording votes, and maintaining audit logs.

## Features

- **Multi-level Election Support**
  - National positions (President, Vice President, Senator)
  - Local positions (Governor, Mayor, Councilor, etc.)
  - Support for multiple cities and regions

- **Admin Dashboard**
  - Candidate management (Add, Update, Delete)
  - Real-time vote monitoring
  - Comprehensive audit logging
  - User-friendly GUI interface

- **Security Features**
  - Secure login system
  - Audit trail for all system actions
  - Data validation and integrity checks

## Prerequisites

- Java Development Kit (JDK) 24 or later
- Eclipse IDE (Latest version recommended)
- Maven 3.6.0 or later

## Installation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/VotingSystems.git
   cd VotingSystems
   ```

2. **Import into Eclipse**
   - Open Eclipse IDE
   - Go to File → Import
   - Select "Existing Maven Projects"
   - Browse to the cloned repository directory
   - Click "Finish"

3. **Project Setup**
   - Right-click on the project in Eclipse
   - Select "Maven" → "Update Project"
   - Check "Force Update of Snapshots/Releases"
   - Click "OK"

## Running the Project

1. **In Eclipse:**
   - Locate `src/main/java/com/mycompany/votingsystems/VotingSystem.java`
   - Right-click on the file
   - Select "Run As" → "Java Application"

2. **From Command Line:**
   ```bash
   mvn clean package
   java -jar target/VotingSystems-1.0-SNAPSHOT.jar
   ```

## Project Structure

```
VotingSystems/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/mycompany/votingsystems/
│   │   │       ├── gui/           # GUI components
│   │   │       ├── model/         # Data models
│   │   │       └── util/          # Utility classes
│   │   └── resources/             # Configuration files
│   └── test/                      # Test cases
├── data/                          # Data storage
└── pom.xml                        # Maven configuration
```

## Dependencies

- **JUnit 5.9.2**: For unit testing
- **Apache Commons CSV 1.10.0**: For CSV file handling
- **Apache Commons Lang 3.12.0**: For utility functions

## Development

### Adding New Features

1. Create a new branch for your feature
2. Implement the feature following the existing code structure
3. Add appropriate unit tests
4. Submit a pull request

### Code Style

- Follow Java naming conventions
- Use meaningful variable and method names
- Add comments for complex logic
- Keep methods focused and concise

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support, please open an issue in the GitHub repository or contact the maintainers.

## Acknowledgments

- Philippine Commission on Elections (COMELEC) for election guidelines
- All contributors who have helped shape this project 