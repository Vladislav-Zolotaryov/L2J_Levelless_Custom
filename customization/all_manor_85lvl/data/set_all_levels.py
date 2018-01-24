
with open('seeds.csv', 'r') as seeds, open('new_seeds.csv', 'w') as new_seeds:
    for line in seeds:
        if line[0].isdigit():
            line_items = line.split(';')
            if (len(line_items) > 1):
                line_items[1] = '85'
                line = ';'.join(line_items)
        new_seeds.write(line)
