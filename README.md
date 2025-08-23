# Reaction Sorter
A simple Spring Boot application for (hopefully) quicker sorting of reaction images and videos.

Download latest version: [Download Link Goes Here](#)

## Features
### Display
- Displays images and videos from Working Directory
- Images
  - JPG/JPEG, PNG, GIF
- Videos
  - MP4, WEBM
  - Seekbar with time elapsed/remaining
  - Configurable autoplay and volume
- File details
  - Name, Type/Extension, Dimensions, Size

### Actions
- Move
  - Move from Working Directory to Target Directory
  - Rename on move
- Skip
  - Skip current file without altering name or location
- Undo
  - Undo last Move or Skip
  - Will undo renaming if applicable
  - No history beyond last action
- End
  - End sorting at any point in queue
  - Prompted for confirmation

### Preferences
- All preferences saved locally
- Default Working & Target Directories (directory browser)
- Confirmation of Move (toggle)
- Video Preferences
  - Autoplay (toggle)
  - Persistent Volume (toggle)
  - Default Volume (slider)