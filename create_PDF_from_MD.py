import pypandoc

pypandoc.download_pandoc()

md = './README.md'
pdf = './README.pdf'
out = pypandoc.convert_file(md, 'pdf', outputfile=pdf, 
                            extra_args=['-V', 'geometry:margin=1in'])
