#!/bin/bash

set -e
set -o pipefail

cd "$(dirname "$0")"

IMG="![ᏣᎳᎩ-ᏧᎴᎯᏌᏅᎯ-ᎤᏍᏛᎪᏍᏙᏗ.png](https://cdn.steemitimages.com/DQmbrTUET1HhhfPDDVSqaDzXB6m9Xicrq1qRBYB2DHWb6Pw/%E1%8F%A3%E1%8E%B3%E1%8E%A9-%E1%8F%A7%E1%8E%B4%E1%8E%AF%E1%8F%8C%E1%8F%85%E1%8E%AF-%E1%8E%A4%E1%8F%8D%E1%8F%9B%E1%8E%AA%E1%8F%8D%E1%8F%99%E1%8F%97.png)"

when=$(git log -1 --pretty=format:'%ci')

mkdir tmp 2> /dev/null || true
touch tmp/log.old
git log --branches=\* --after="1 week ago" > tmp/log.new
diff tmp/log.new tmp/log.old > /dev/null 2>&1 && exit
if [ ! -s tmp/log.new ]; then exit; fi

echo "Posting new message"
msgfile="tmp/msg.txt"
cp /dev/null "$msgfile"
echo "title: Updates for $(basename $(pwd)) within the past week. [$when]"  >> "$msgfile"
echo "tags: utopian-io steemdev java steemj cherokee-language"  >> "$msgfile"
echo "format: markdown"  >> "$msgfile"
echo "" >> "$msgfile"
echo "${IMG}" >> "$msgfile"
echo "" >> "$msgfile"
echo "## $(basename $(pwd))" >> "$msgfile"
echo "Updates for $(basename $(pwd)) in the past week. $when" >> "$msgfile"
echo "" >> "$msgfile"
echo "" >> "$msgfile"
git log --branches=\* --after="1 week ago" | sed 's/<.*@.*>/[email redacted]/g' | sed 's/^commit /#### commit /g' >> "$msgfile"
echo "" >> "$msgfile"
echo "" >> "$msgfile"
sed -i 's/\&/\&amp;/g' "$msgfile"
sed -i 's/</\&lt;/g' "$msgfile"
sed -i 's/>/\&gt;/g' "$msgfile"
sed -i 's/\t/    /g' "$msgfile"
sed -i 's|  |\&nbsp; |g' "$msgfile"
java -jar ~/git/SteemCliPoster/build/libs/SteemCliPoster.jar \
	--auth-file ~/.steem/magali.properties \
	--no-escape \
	--file "$msgfile"
cp tmp/log.new tmp/log.old

