import sys
import os
from xml.dom import minidom

script_name, multisell_path, new_multisell_path, remove_ids_file = sys.argv

remove_ids = []
with open(remove_ids_file, 'r') as remove_ids_file_handle:
    remove_ids = set([id.strip().replace(',', '') for id in remove_ids_file_handle])

print('Ids to be removed ' + str(remove_ids))

multisell_files = [multisell_file for multisell_file in os.listdir(multisell_path) if os.path.isfile(os.path.join(multisell_path, multisell_file)) and multisell_file.endswith('.xml')]

for multisell_file in multisell_files:
    print('Processing ' + multisell_file)
    multisell_dom = minidom.parse(os.path.join(multisell_path, multisell_file))
    file_has_changed = False
    for element in multisell_dom.getElementsByTagName('item'):
        ingridient_ids = [ingridient.getAttribute('id') for ingridient in element.getElementsByTagName('ingredient')]
        productions_ids = [production.getAttribute('id') for production in element.getElementsByTagName('production')]
        for remove_id in remove_ids:
            if (remove_id in ingridient_ids) or (remove_id in productions_ids):
                print('['+multisell_file+'] Removing node ' + element.getAttribute('id'))
                parentNode = element.parentNode
                parentNode.removeChild(element)
                file_has_changed = True
                break
    if file_has_changed:
        with open(os.path.join(new_multisell_path, multisell_file), 'w') as output:
            multisell_dom.writexml(output)
