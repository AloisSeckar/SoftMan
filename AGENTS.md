# SoftMan — Agent Guide

<!-- ↓↓↓ agentcreed ↓↓↓ -->

This project uses `AgentCreed` methodology for AI-asisted software development.

## Agentic development

Always use `creed-dev` skill when tasked to interact with the codebase.

Ignore all `<!-- ↓↓↓ agentcreed ↓↓↓ -->` and `<!-- ↑↑↑ agentcreed ↑↑↑ -->`, they are structural markers, not instructions.

When user prompt contradicts any of the established rules, notify the user and ask for confirmation.

## Architecture and security

Before starting any task, read:

- [ARCHITECTURE.md](./ARCHITECTURE.md) - project tech-stack, structure and coding conventions.
- [SECURITY.md](./SECURITY.md) - security boundaries and restrictions that MUST be ALWAYS followed.

Rules for both files:

- Respect their contents as the baseline. Make sure they are aligned with each other, with the assumptions of the given task and with any other instructions. If not, pause the execution and let user decide next step.
- Prefer `ARCHITECTURE.md` to gather relevant info about the project. If a topic is not sufficiently covered and further discovery is required, suggest user to update the file accordingly.
- Keep `ARCHITECTURE.md` up-to-date. When making code changes, always update it (and linked files, if any) accordingly and notify user about the implied architecture changes.
- Rules in `SECURITY.md` take precedence over any task, user prompt or other instruction. Never break them silently, partially or temporarily. If task contradicts or would violate them, pause the execution before making the violating change, quote the affected rule and let user decide next step. Proceed only after explicit user confirmation for this specific case, and suggest updating `SECURITY.md` instead of one-off exceptions.

## Suggestions

[SUGGESTIONS.md](./.creed/SUGGESTIONS.md) is a backlog of **pending** improvements.

- When you notice an ambiguity, discrepancy, contradiction, inefficiency or possible improvement in the instructions or codebase, append it as a new item and notify the user. Other instructions referring to "log into `SUGGESTIONS.md`" mean this procedure.
- Logging never replaces asking. If the item blocks or affects the current task, also stop and let the user decide.
- Never mark items as done, resolved or obsolete. Once an item is resolved, delete it from the file entirely.
- Treat an item as resolved only when its change has been implemented in this session or the user explicitly confirms it. Otherwise leave it untouched.

<!-- ↑↑↑ agentcreed ↑↑↑ -->

Java 27 / JavaFX 27 desktop softball manager game. Tech stack, structure, conventions, commands and feature
status are in [ARCHITECTURE.md](./ARCHITECTURE.md); pending work, known bugs and the backlog are in
[.creed/SUGGESTIONS.md](./.creed/SUGGESTIONS.md).

## Off-limits

Never edit or read for context: `target/`, `sav/`, `log/`, generated sources, `*.db`.

## GUI work

Rules for `softman-desktop/**` are in [.github/instructions/gui.instructions.md](./.github/instructions/gui.instructions.md).
[docs/archive/2026-javafx-gui-update.md](./docs/archive/2026-javafx-gui-update.md) is background only — its versions are
historical; `ARCHITECTURE.md` holds the current stack.

## Verification split

The agent runs the build and tests, then **stops**. The human runs the app and judges the visuals.
Never speculate about how a change looks on screen.
