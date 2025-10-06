# Contributing to Cloud Sync Application

Thank you for your interest in contributing to the Cloud Sync Application!

## Getting Started

1. Fork the repository
2. Clone your fork: `git clone https://github.com/your-username/git-repo.git`
3. Create a new branch: `git checkout -b feature/your-feature-name`
4. Make your changes
5. Test your changes thoroughly
6. Commit your changes: `git commit -m "Add your descriptive commit message"`
7. Push to your fork: `git push origin feature/your-feature-name`
8. Open a Pull Request

## Development Setup

1. Install dependencies:
```bash
npm install
```

2. Copy `.env.example` to `.env` and configure your environment variables:
```bash
cp .env.example .env
```

3. Set up OAuth credentials:
   - Google Cloud Console: https://console.cloud.google.com/
   - Microsoft Azure Portal: https://portal.azure.com/

4. Start the development server:
```bash
npm run dev
```

## Code Style

- Use consistent indentation (2 spaces)
- Follow existing code patterns
- Add comments for complex logic
- Use meaningful variable and function names

## Testing

Before submitting a Pull Request:
- Test all authentication flows
- Test all API endpoints
- Ensure no syntax errors
- Verify error handling

## Pull Request Guidelines

- Keep PRs focused on a single feature or fix
- Update documentation if needed
- Describe your changes clearly
- Reference related issues

## Questions?

Feel free to open an issue if you have questions or need help!
