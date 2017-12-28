package IVR_sanity_Tests;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	
	Configuration_backup_restore_auto_backup____Test428.class		,
	Configuration_backup_restore_manual_backup____Test427.class		,
	Configuration_backup_restore_restore____Test429.class			,
	Management_acds_create_edit_delete.class						,	
	Management_agents_create_edit_delete.class						,
	Management_business_hours_create_edit_delete.class				,
	Management_export_agents.class									,
	Management_export_business_hours.class							,
	Management_export_groups.class									,
	Management_export_holidays.class								,
	Management_export_ivrs.class									,
	Management_export_ivr_endpoints.class							,
	Management_export_music_on_hold.class							,
	Management_export_prompts.class									,
	Management_export_queues.class									,
	Management_groups_create_edit_delete.class						,
	Management_holidays_create_edit_delete.class					,
	Management_import_export_export_all_create_all_templates.class	,
	Management_ivr_create_edit_delete.class							,
	Management_ivr_endPoints_create_delete.class					,
	Management_languages_create_edit_delete.class					,
	Management_load_samples.class									,
	Management_mohs_create_edit_delete.class						,
	Management_prompts_create_prompt_based_file.class				,
	Management_prompts_create__prompt_based_text.class				,
	Management_proxy_sip_trunks_create_edit_delete.class			,
	Management_queues_create_edit_delete.class
})

public class TestSuite {

}
