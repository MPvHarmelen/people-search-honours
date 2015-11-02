../trec_eval.9.0/trec_eval \
    -m all_trec \
    -q \
    ../tu-expert-collection/qrels/expert_finding/GT1_self_selected_all_experts.qrel \
    $1 \
    > trec_out/$(expr $1 : '.*/\(.*\)\.out').gt1

../trec_eval.9.0/trec_eval \
    -m all_trec \
    -q \
    ../tu-expert-collection/qrels/expert_finding/GT5_judged_system_generated_graded.qrel \
    $1 \
    > trec_out/$(expr $1 : '.*/\(.*\)\.out').gt5
