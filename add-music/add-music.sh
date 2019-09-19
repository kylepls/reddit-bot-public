# Instructions:
# 1. Place music in this folder
# 2. Run command
# 3. Converted music gets added to music directory
cd ../add-music || exit
for file in *.mp3 *.wav
do
  filename=$(basename -- "$file")
  filename="${filename%.*}"
  output="../music/$filename.mp3"
  echo "Converting $file -> $output"
  ffmpeg -loglevel error -i "$file" -af "loudnorm" -y "$output"
  if [ ! -f "$output" ]; then
      echo "Unable to convert $file to $output"
  fi
done