# people-search-honours

## How to run Indri and Ears with the files in this repository
First of all, if you're not going to change any settings, you probably don't want
to run Indri or Ears again, but just use one of the output files (to be found in `output/`).

### Creating an index with IndriBuildIndex
To create an index choose an appropriate Indri configuration file (to be found in `confs-indri`). Make sure the paths specified in the configuration file are correct relative to you working directory and run the following command:

    `IndriBuildIndex path/to/conf/file.conf`

In an Indri configuration file the `index` parameter specifies the location for the index to be stored at. The `corpus` parameters specify which files to use as corpus and what format they are in.

### Creating a ranking with Ears
After you have built an Index you can take some queries and use Ears to run the queries on the Index. This takes a *long* time (about 10 min on a 2.4 GHz Intel). Again, choose an appropriate configuration file (to be found in `confs-ears` this time) and make sure the paths specified in the configuration file are correct relative to you working directory. Run the following command, sit back and relax.

    `ears ef path/to/conf_file.conf`

This time the configuration file is a bit more interesting, but before I tell you what it does, I would like to share some thoughts on how to use these configuration files.

I think it would be good policy to create a separate configuration file each time it is changed, with a nicely descriptive name. Please also change the `outputFile` setting to have the same name as the configuration file. Like this all experiment settings and results will be stored and traceable.

Yes. That is would you should do. Now let that be a lesson to you. Now you're allowed to ride the bike too! Most settings are self-explanatory, however the settings you shouldn't (have to) touch are:

    - `outputFileFormat`
    - `smoothingMethod`
    - `smoothingParam`
    - `associationFileFormat`
    - `associationFile`
    - `task` (although I don't think changing it has any effect)

Settings you could change:

    - `runID`   Stores an identifier for this run, for your convenience
    - `model`
    - `index`
    - `queryFile`
    - `outputFile`

For more info on which parameters you could use, check the [manual](https://code.google.com/p/ears/wiki/Parameters).

## Running trec_eval

 - `trec_eval -m all_trec -q <ground_truth> <model_result>`

# Naming convention
All files that differ for different experiments should have their as follows:

`<corpus id>-<language>-model<model number>-<smoothing method>-<experiment id>.<extension>`

Like this, we can easily keep track of what the \*\*\*\* we did during which experiment.

# Running EARS and trec_eval in a oner
Make sure your ears conf file ends with `.conf` and saves the output to `output/some_name.out`. To run ears and trec_eval in one command, cd to the base directory of this repository (probably `people-search-honours`) and run the following command:

 - `ears_and_trec.sh path/to/conf_file.conf`

And voilà, you can find your trec results under `trec_out`, with the same filename as the conf file, but with `.conf` replaced by `.gt1` and `.gt5` to refer to the ground truths.
