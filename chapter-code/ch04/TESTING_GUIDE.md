# Chapter 4 Testing Guide

This guide helps verify that all the improvements made to Chapter 4 work correctly.

## Prerequisites
- Java 21 or later installed
- Gradle (included via wrapper)

## Test 1: Basic Functionality Test

### Step 1: Clean Start
```bash
# Remove any existing data directory
rm -rf data/

# Build and run the application
./gradlew run
```

### Step 2: Add Songs
When the application starts, you should see:
```
ℹ️ 提示：未找到播放列表文件 data/playlist.csv，将为您创建一个新的。
```

Add 2-3 songs:
1. Choose option `1` (添加歌曲)
2. Enter song details, for example:
   - Title: Bohemian Rhapsody
   - Artist: Queen
   - Duration: 355

### Step 3: View Playlist
Choose option `2` to verify songs were added correctly.

### Step 4: Exit and Verify File Creation
1. Choose option `0` to exit
2. You should see: `✅ 播放列表已成功保存到 data/playlist.csv`
3. Verify the `data/` directory was created automatically
4. Check the contents of `data/playlist.csv`

### Step 5: Verify Persistence
1. Run the application again: `./gradlew run`
2. You should see: `✅ 播放列表已从 data/playlist.csv 成功加载。`
3. Choose option `2` to verify all songs are still there

**Expected Result**: ✅ All songs persist across application restarts

---

## Test 2: Edge Case - Empty Lines

### Step 1: Add Empty Lines to CSV
Edit `data/playlist.csv` and add some empty lines:
```csv
Bohemian Rhapsody,Queen,355

Stairway to Heaven,Led Zeppelin,482

Hotel California,Eagles,391
```

### Step 2: Load and Verify
1. Run the application: `./gradlew run`
2. Choose option `2` to view playlist
3. All songs should load correctly, ignoring empty lines

**Expected Result**: ✅ Empty lines are gracefully ignored

---

## Test 3: Edge Case - Whitespace in Fields

### Step 1: Add Whitespace to CSV
Edit `data/playlist.csv` to add spaces around fields:
```csv
 Bohemian Rhapsody , Queen , 355
Stairway to Heaven,  Led Zeppelin  ,482
```

### Step 2: Load and Verify
1. Run the application: `./gradlew run`
2. Choose option `2` to view playlist
3. Songs should load correctly with whitespace trimmed

**Expected Result**: ✅ Whitespace is automatically trimmed

---

## Test 4: Error Handling - Corrupted File

### Step 1: Create Invalid CSV (Wrong Field Count)
Edit `data/playlist.csv`:
```csv
Bohemian Rhapsody,Queen,355
Invalid Song,Queen
Stairway to Heaven,Led Zeppelin,482
```

### Step 2: Attempt to Load
1. Run the application: `./gradlew run`
2. You should see an error message:
   ```
   ❌ 错误：解析文件内容失败，文件格式可能不正确。 (CSV格式错误：期望3个字段，实际得到 2 个字段)
   ```

**Expected Result**: ✅ Clear error message indicating the problem

### Step 3: Create Invalid CSV (Non-numeric Duration)
Edit `data/playlist.csv`:
```csv
Bohemian Rhapsody,Queen,355
Invalid Song,Queen,not_a_number
Stairway to Heaven,Led Zeppelin,482
```

### Step 4: Attempt to Load
1. Run the application: `./gradlew run`
2. You should see an error message:
   ```
   ❌ 错误：解析文件内容失败，文件格式可能不正确。 (CSV格式错误：时长必须是数字，得到的是 'not_a_number')
   ```

**Expected Result**: ✅ Clear error message indicating the specific problem

---

## Test 5: Error Handling - File Permissions

### macOS/Linux

#### Step 1: Make File Read-Only
```bash
chmod 444 data/playlist.csv
```

#### Step 2: Try to Save
1. Run the application: `./gradlew run`
2. Add a new song
3. Exit (option `0`)
4. You should see:
   ```
   ❌ 错误：无法保存播放列表。请检查文件权限或磁盘空间。
   ```

#### Step 3: Restore Permissions
```bash
chmod 644 data/playlist.csv
```

**Expected Result**: ✅ Graceful error handling for permission issues

### Windows

#### Step 1: Make File Read-Only
1. Right-click `data/playlist.csv`
2. Properties → Check "Read-only"

#### Step 2: Try to Save
1. Run the application
2. Add a new song
3. Exit
4. You should see an error message about being unable to save

#### Step 3: Restore Permissions
1. Right-click `data/playlist.csv`
2. Properties → Uncheck "Read-only"

**Expected Result**: ✅ Graceful error handling for permission issues

---

## Test 6: Directory Auto-Creation

### Step 1: Remove Data Directory
```bash
rm -rf data/
```

### Step 2: Run and Add Song
1. Run the application: `./gradlew run`
2. Add a song
3. Exit

### Step 3: Verify
Check that `data/` directory was automatically created.

**Expected Result**: ✅ Directory is created automatically when saving

---

## Test 7: CSV Comma Problem (Known Limitation)

### Step 1: Try to Add Song with Comma in Title
1. Run the application
2. Add a song with title: `No, Woman, No Cry`
3. Artist: `Bob Marley`
4. Duration: `237`
5. Exit

### Step 2: Restart and Check
1. Run the application again
2. You should see an error:
   ```
   ❌ 错误：解析文件内容失败，文件格式可能不正确。 (CSV格式错误：期望3个字段，实际得到 4 个字段)
   ```

**Expected Result**: ✅ This is a known limitation documented in the code comments

**Note**: This demonstrates why more robust formats like JSON might be preferred for production applications.

---

## Test 8: Unit Test for Song Serialization

You can also test the serialization logic programmatically:

```java
// Test valid CSV
Song song1 = Song.fromCsvString("Bohemian Rhapsody,Queen,355");
assert song1.title().equals("Bohemian Rhapsody");
assert song1.artist().equals("Queen");
assert song1.durationInSeconds() == 355;

// Test round-trip
String csv = song1.toCsvString();
Song song2 = Song.fromCsvString(csv);
assert song1.equals(song2);

// Test error handling
try {
    Song.fromCsvString("Invalid,CSV");
    assert false : "Should have thrown exception";
} catch (IllegalArgumentException e) {
    assert e.getMessage().contains("期望3个字段");
}

try {
    Song.fromCsvString("Title,Artist,NotANumber");
    assert false : "Should have thrown exception";
} catch (IllegalArgumentException e) {
    assert e.getMessage().contains("时长必须是数字");
}
```

---

## Summary of Improvements Verified

✅ **Directory Auto-Creation**: `data/` directory is created automatically
✅ **File Persistence**: Songs persist across application restarts
✅ **Empty Line Handling**: Empty lines in CSV are gracefully ignored
✅ **Whitespace Trimming**: Leading/trailing whitespace is automatically removed
✅ **Input Validation**: Invalid field counts are detected and reported
✅ **Number Validation**: Non-numeric durations are detected and reported
✅ **Permission Errors**: File permission issues are handled gracefully
✅ **User-Friendly Messages**: All error messages are clear and helpful
✅ **File Not Found**: Missing files are handled gracefully with informative messages

---

## Cleanup

After testing, you can clean up:
```bash
rm -rf data/
./gradlew clean
```

---

## Notes for Students

This testing guide demonstrates the importance of:
1. **Edge Case Testing**: Testing with unusual but valid inputs
2. **Error Condition Testing**: Deliberately creating error conditions
3. **Persistence Testing**: Verifying data survives application restarts
4. **Cross-Platform Considerations**: Different operating systems may behave differently

Remember: **Robust code handles not just the happy path, but also the edge cases and error conditions gracefully.**

