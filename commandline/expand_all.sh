LANGUAGES="nl uk"
WORDKS="1 2 4 8 16"
WORKING_DIR="../java-prf/"

cd $WORKING_DIR
for TOPICS_LANG in $LANGUAGES; do
    for WORDK in $WORDKS; do
        export TOPICS_LANG;
        export WORDK;
        make run
    done
done
