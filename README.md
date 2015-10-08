# people-search-honours

## How to run Indri and Ears with the files in this repository
First of all, if you're not going to change any settings, you probably don't want
to run Indri or Ears again, but just use one of the output files (to be found in `output/`).

### Creating an index with IndriBuildIndex
To create an index choose an appropriate Indri configuration file (to be found in `confs-indri`). Make sure the paths specified are correct relative to you working directory and run the following command:

    `IndriBuildIndex path/to/conf/file.conf`

In an Indri configuration file the `index` parameter specifies the location for the index to be stored at. The `corpus` parameters specify which files to use as corpus and what format they are in.

### Creating a ranking with Ears
After you have built an Index you can take some queries and use Ears to run the queries on the Index. This takes a *long* time (about 10 min on a 2.4 GHz Intel). Again, choose an appropriate configuration file (to be found in `confs-ears` this time) and make sure the paths specified are correct relative to you working directory. Run the following command, sit back and relax.

    `ears ef path/to/conf/file.conf`

This time the configuration file is a bit more interesting, but before I tell you what it does, I would like to share some thoughts on how to use these configuration files.

I think it would be good policy to create a separate configuration file each time it is changed, with a nicely descriptive name. Please also change the `outputFile` setting to have the same name as the configuration file. Like this all experiment settings and results will be stored and traceable.

Yes. That is would you should do. Now let that be a lesson to you. Now you're allowed to ride the bike too! Most settings are self-explanatory, however the settings you shouldn't (have to) touch are:

    - `outputFileFormat`
    - `smoothingMethod`
    - `associationFileFormat`
    - `associationFile`
    - `task` (although I don't think changing it has any effect)

These already have a working value and things could break if you change them. Settings I have no idea what they do are:

    - `runID`   probably adds some identifier to the run, but I have no idea where and how it is stored
    - `outputNum`   I just have no clue what this setting does

Settings you could change:

    - `model`
    - `index`
    - `queryFile`
    - `outputFile`
