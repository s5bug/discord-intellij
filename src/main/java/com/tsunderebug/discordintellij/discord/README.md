#Discord Presence Format
This plugin currently supports three modes for discord presence.
These modes are automatically configured based upon the current
state of the IDE.

## Default Mode
This mode is used only during startup or if no projects are 
currently open. This mode shows the tool being used and the 
detailed build information for the tool. It displays the tool
logo.

![Image showing default mode example][default_mode]

## Project Mode
This mode is used if a project is currently open. This mode
shows the tool being used and the  detailed build information
for the tool. It shows what project is open and it displays the
tool logo.

![Image showing project mode example][project_mode]

## Editing Mode
This mode is used by default as long as a file is being edited.
This mode shows the tool being used and the project under 
development. It shows the name and the type of file being edited.
It displays the file type image wtih a smaller version of the tool
logo.

![Image showing editing mode example][editing_mode]

[default_mode]: default_mode.png "Default mode for discord presence plugin"
[project_mode]: project_mode.png "Project mode for discord presence plugin"
[editing_mode]: editing_mode.png "Editing mode for discord presence plugin"
