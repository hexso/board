# -*- coding:utf-8 -*-
import os
import subprocess

DEST_DIR = "D:\\new"


def get_mp4_list(root_folder):
    file_list = list()
    for foldername, subfolders, filenames in os.walk(root_folder):
        for filename in filenames:
            if filename.lower().endswith(".mp4"):
                folder_path = os.path.join(root_folder, foldername)

                file_path = os.path.join(folder_path, filename)
                file_list.append(file_path)
    return file_list


def change_mp4_to_hls(file_path):
    try:
        file_name_mp4 = os.path.basename(file_path)
        file_name = os.path.splitext(file_name_mp4)[0] #확장자명 제거
        dest_path = DEST_DIR +'\\' + file_name
        os.makedirs(dest_path)
    except FileExistsError:
        pass
    ffmpeg_cmd = "ffmpeg -i \"{}\" -codec: copy -start_number 0 -hls_time 10 -hls_list_size 0 -f hls \"{}.m3u8\"".format(file_path, os.path.join(dest_path,"index"))
    print(ffmpeg_cmd)
    os.system(ffmpeg_cmd)

if __name__ == "__main__":
    root_dir = "D:\\old"
    mp4_list = get_mp4_list(root_dir)
    print(mp4_list)
    #change_mp4_to_hls(mp4_list[1])
