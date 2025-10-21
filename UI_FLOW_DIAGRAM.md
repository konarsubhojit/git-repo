# UI Flow Diagram - Folder Sync Feature

## User Journey: Setting Up a Folder Sync

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         Main Activity (Home)                            │
│                                                                         │
│  ┌─────────────────────────────────────────────────────────┐          │
│  │  Google Drive                                            │          │
│  │  ● Connected: user@gmail.com                             │          │
│  │  [Change Account]                                        │          │
│  └─────────────────────────────────────────────────────────┘          │
│                                                                         │
│  ┌─────────────────────────────────────────────────────────┐          │
│  │  Microsoft OneDrive                                      │          │
│  │  ○ Not connected                                         │          │
│  │  [Add OneDrive Account]                                  │          │
│  └─────────────────────────────────────────────────────────┘          │
│                                                                         │
│  [Manage Folder Sync] ◄── NEW FEATURE                                 │
│                                                                         │
│  [Sync Data]                                                           │
│                                                                         │
└───────────────────────┬─────────────────────────────────────────────────┘
                        │ User taps "Manage Folder Sync"
                        ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                   Sync Configuration List Activity                      │
│  ┌─────────────────────────────────────────────────────────┐          │
│  │  Manage Sync Configurations                             │          │
│  └─────────────────────────────────────────────────────────┘          │
│                                                                         │
│  When Empty:                                                           │
│  ┌─────────────────────────────────────────────────────────┐          │
│  │              [Large Folder Icon]                         │          │
│  │                                                           │          │
│  │        No sync configurations yet                        │          │
│  │                                                           │          │
│  │   Tap the + button to add a new sync configuration      │          │
│  └─────────────────────────────────────────────────────────┘          │
│                                                                         │
│  With Configurations:                                                  │
│  ┌─────────────────────────────────────────────────────────┐          │
│  │ [Google Icon]  Two-Way Sync              Active ✓       │          │
│  │ ─────────────────────────────────────────────────────   │          │
│  │ 📁 /storage/emulated/0/Documents                        │          │
│  │ ☁️  Documents                                            │          │
│  └─────────────────────────────────────────────────────────┘          │
│  ┌─────────────────────────────────────────────────────────┐          │
│  │ [OneDrive Icon]  Upload then Delete     Active ✓        │          │
│  │ ─────────────────────────────────────────────────────   │          │
│  │ 📁 /storage/emulated/0/Photos                           │          │
│  │ ☁️  Backup/Photos                                        │          │
│  └─────────────────────────────────────────────────────────┘          │
│                                                                         │
│                                                        [+ FAB] ◄────────┤
│                                                                         │
└───────────────────────┬─────────────────────────────────────────────────┘
                        │ User taps FAB (+) or a configuration card
                        ▼
┌─────────────────────────────────────────────────────────────────────────┐
│              Folder Sync Configuration Activity                         │
│                                                                         │
│  Configure Folder Sync                                                 │
│  Set up automatic synchronization between local and cloud folders     │
│                                                                         │
│  ┌─────────────────────────────────────────────────────────┐          │
│  │ Folder Configuration                                     │          │
│  │                                                           │          │
│  │ Local Folder Path                                        │          │
│  │ 📁 [/storage/emulated/0/Documents____________]          │          │
│  │                                                           │          │
│  │ [Browse Folder]                                          │          │
│  │                                                           │          │
│  │ Cloud Folder Path                                        │          │
│  │ ☁️  [Documents___________________________]              │          │
│  └─────────────────────────────────────────────────────────┘          │
│                                                                         │
│  ┌─────────────────────────────────────────────────────────┐          │
│  │ Sync Settings                                            │          │
│  │                                                           │          │
│  │ Cloud Provider                                           │          │
│  │ ☁️  [Google Drive                    ▼]                 │          │
│  │                                                           │          │
│  │ Sync Mode                                                │          │
│  │ 🔄 [Two-Way Sync                     ▼]                 │          │
│  │     ├─ Upload Only                                       │          │
│  │     ├─ Upload then Delete                                │          │
│  │     ├─ Download Only                                     │          │
│  │     ├─ Download then Delete                              │          │
│  │     └─ Two-Way Sync (selected)                           │          │
│  │                                                           │          │
│  │ ┌─────────────────────────────────────────────────┐    │          │
│  │ │ Delete Delay (shown for upload/download-delete) │    │          │
│  │ │                                                   │    │          │
│  │ │ 7 days                                           │    │          │
│  │ │                                                   │    │          │
│  │ │ ●───────────●────────────────────                │    │          │
│  │ │ 0                                            30  │    │          │
│  │ │                                                   │    │          │
│  │ │ Files will be deleted after the specified delay │    │          │
│  │ └─────────────────────────────────────────────────┘    │          │
│  └─────────────────────────────────────────────────────────┘          │
│                                                                         │
│  [Cancel]                        [Save Configuration ✓]                │
│                                                                         │
└───────────────────────┬─────────────────────────────────────────────────┘
                        │ User taps "Save Configuration"
                        ▼
            Configuration saved and user returns to list
                        │
                        ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                   Sync Configuration List Activity                      │
│                                                                         │
│  ┌─────────────────────────────────────────────────────────┐          │
│  │ [Google Icon]  Two-Way Sync              Active ✓       │          │
│  │ ─────────────────────────────────────────────────────   │          │
│  │ 📁 /storage/emulated/0/Documents                        │          │
│  │ ☁️  Documents                                            │          │
│  └─────────────────────────────────────────────────────────┘          │
│                                                                         │
│  Snackbar: "Configuration saved successfully"                          │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

## Interaction Details

### Main Activity
**Elements:**
- Account connection status cards (Google Drive, OneDrive)
- "Manage Folder Sync" button (NEW)
- "Sync Data" button

**Actions:**
- Tap "Manage Folder Sync" → Navigate to Sync Configuration List

### Sync Configuration List Activity
**Elements:**
- Toolbar with title
- RecyclerView of configuration cards
- Empty state (when no configs)
- FAB (+) button

**Card Contents:**
- Provider icon (Google/OneDrive)
- Sync mode name
- Status badge (Active/Disabled)
- Local folder path
- Cloud folder path

**Actions:**
- Tap Card → Show options menu
  - View Details
  - Edit
  - Toggle Enable/Disable
  - Delete
- Long Press Card → Delete confirmation
- Tap FAB → Navigate to Configuration Activity

### Folder Sync Configuration Activity
**Sections:**

1. **Folder Configuration Card**
   - Local Folder Path input
   - Browse Folder button
   - Cloud Folder Path input

2. **Sync Settings Card**
   - Cloud Provider dropdown
     - Google Drive
     - OneDrive
   
   - Sync Mode dropdown
     - Upload Only
     - Upload then Delete
     - Download Only
     - Download then Delete
     - Two-Way Sync
   
   - Delete Delay (conditional)
     - Slider: 0-30 days
     - Value display
     - Hint text

**Actions:**
- Select Provider → Update provider selection
- Select Sync Mode → Update mode, show/hide delay slider
- Adjust Slider → Update delay value display
- Tap Cancel → Return without saving
- Tap Save → Validate, save, return with success message

## Sync Mode UI Behavior

### Upload Only / Download Only / Two-Way Sync
```
Delete Delay Section: HIDDEN
User sees only folder paths, provider, and sync mode
```

### Upload then Delete / Download then Delete
```
Delete Delay Section: VISIBLE
┌───────────────────────────────────┐
│ Delete Delay                      │
│                                   │
│ 7 days                            │
│                                   │
│ ●───────●────────────────────     │
│ 0                            30   │
│                                   │
│ Files deleted after 7 days        │
└───────────────────────────────────┘
```

## User Feedback

### Success States
- ✅ **Snackbar:** "Configuration saved successfully"
- ✅ **Snackbar:** "Configuration deleted"
- ✅ **Snackbar:** "Configuration enabled/disabled"
- ✅ **Visual:** Status icon changes (✓ for active, ⚠ for disabled)

### Error States
- ⚠️ **Snackbar:** "Please enter a local folder path"
- ⚠️ **Snackbar:** "Please enter a cloud folder path"
- ⚠️ **Snackbar:** "Invalid folder path"
- ⚠️ **Snackbar:** "Network error"

### Loading States
- 🔄 **Progress:** During API calls
- 🔄 **Disabled:** Buttons disabled during operations

## Material Design Elements

### Visual Components
- **Cards:** Elevated cards with 12dp corner radius
- **Buttons:** Material buttons with appropriate styles
  - Primary (filled): Save, Sync
  - Secondary (outlined): Cancel, Browse
- **FAB:** Floating action button with icon
- **Dropdowns:** Exposed dropdown menus
- **Slider:** Material continuous slider
- **Icons:** Material Design icons throughout
- **Snackbars:** Bottom slide-up notifications

### Color Usage
- **Primary:** Main actions and headers
- **Secondary:** Supporting elements
- **Success Green:** Active/enabled states
- **Error Red:** Disabled/error states
- **Neutral Gray:** Secondary text

### Typography
- **24sp Bold:** Page titles
- **18sp Bold:** Section headers
- **16sp Bold:** Card titles
- **14sp Regular:** Body text
- **12sp Regular:** Hints and secondary info

## Responsive Behavior

### Portrait Mode (Default)
- Scrollable layout ensures all content accessible
- Single column layout
- Full-width cards and buttons

### Landscape Mode
- Same layout, scrollable
- Maintains all functionality
- Material Design elevation preserved

### Tablet (Future)
- Could use two-pane layout
- Master-detail pattern
- List on left, details on right

## Navigation Flow Summary

```
Main Activity
    ↓
    ├─ Tap "Manage Folder Sync"
    ↓
Sync Config List
    ↓
    ├─ Tap FAB (+)
    │   ↓
    │   Folder Config Activity
    │       ↓
    │       Save → Back to List
    │
    ├─ Tap Card
    │   ↓
    │   Options Menu
    │       ↓
    │       Edit → Folder Config Activity
    │       Delete → Confirmation → Delete → Refresh List
    │       Toggle → Update → Refresh List
    │       View → Details Dialog
    │
    ├─ Long Press Card
    │   ↓
    │   Delete Confirmation → Delete → Refresh List
    │
    └─ Back → Main Activity
```

## Accessibility

### Touch Targets
- All interactive elements ≥48dp minimum
- Adequate spacing between elements
- Large tap areas for cards and buttons

### Content Descriptions
- All icons have descriptive labels
- Screen reader support
- Semantic HTML-like structure

### Visual Contrast
- Text meets WCAG AA standards
- Clear visual hierarchy
- Status indicators use both color and icons

### Text Scaling
- Supports Android system text size
- Layouts adapt to larger text
- No fixed heights that clip text

## Empty States

### No Configurations
```
┌─────────────────────────────────┐
│                                 │
│     [Large Folder Icon]         │
│                                 │
│  No sync configurations yet     │
│                                 │
│  Tap the + button to add a new  │
│  sync configuration             │
│                                 │
└─────────────────────────────────┘
```

### No Account Connected (Future)
```
┌─────────────────────────────────┐
│                                 │
│     [Account Icon]              │
│                                 │
│  Connect an account first       │
│                                 │
│  Add Google Drive or OneDrive   │
│  account to get started         │
│                                 │
└─────────────────────────────────┘
```

## Error Handling

### Network Errors
- Clear error message in snackbar
- Retry option when applicable
- Graceful degradation

### Validation Errors
- Inline error messages
- Field highlighting
- Clear guidance on what's wrong

### Permission Errors
- Explanation of why permission needed
- Button to request permission
- Alternative options if available

## Performance Considerations

### List Performance
- RecyclerView with ViewHolder pattern
- Efficient item updates
- Smooth scrolling

### API Calls
- Debounced validation
- Cached responses when appropriate
- Loading indicators for all async operations

### Memory Management
- Proper lifecycle handling
- No memory leaks
- Efficient resource usage
