
import os
import shutil
import py_compile
def clean_desktop_dialog():
    response = input("Would you like to clean up your desktop? yes (1), or no (2)?").lower()

    if response == '1':
        # Code to execute if the user input is 'yes'
        pass
    elif response == '2':
        reason = input("Please specify your reason for not cleaning up: ").lower()
        # Code to handle the reason
    else:
        print("Invalid input. Please enter '1(yes)' or '2(no)'.")

clean_desktop_dialog()






def clean_desktop():
    # Code to clean up the desktop goes here
    pass
audio = (".3ga", ".aac", ".ac3", ".aif", ".aiff",
         ".alac", ".amr", ".ape", ".au", ".dss",
         ".flac", ".flv", ".m4a", ".m4b", ".m4p",
         ".mp3", ".mpga", ".ogg", ".oga", ".mogg",
         ".opus", ".qcp", ".tta", ".voc", ".wav",
         ".wma", ".wv")

video = (".webm", ".MTS", ".M2TS", ".TS", ".mov",
         ".mp4", ".m4p", ".m4v", ".mxf")

img = (".jpg", ".jpeg", ".jfif", ".pjpeg", ".pjp", ".png",
       ".gif", ".webp", ".svg", ".apng", ".avif")

def is_audio(file):
    return os.path.splitext(file)[1] in audio

def is_video(file):
    return os.path.splitext(file)[1] in video

def is_image(file):
    return os.path.splitext(file)[1] in img

def is_screenshot(file):
    name, ext = os.path.splitext(file)
    return (ext in img) and "screenshot" in name.lower()


for file in os.listdir():
    if is_audio(file):
        shutil.move(file, "Users/ADMIN/Documents/audio")
    elif is_video(file):
        shutil.move(file, "Users/ADMIN/Documents/video")
    elif is_image(file):
        if is_screenshot(file):
            shutil.move(file, "Users/ADMIN/Documents/screenshots")
        else:
            shutil.move(file, "Users/ADMIN/Documents/images")

py_compile; clean_desktop
