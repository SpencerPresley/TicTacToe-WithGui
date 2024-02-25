from fpdf import FPDF
from PIL import Image

image_path = './UML_TTTGUIAI.png'
image = Image.open(image_path)

pdf = FPDF()

pdf.add_page()

pdf_w = 210 # width of paper in mm
pdf_h = 297 # height in mm
padding = 10 # padding in mm

# available width and height after accounting for padding
avail_w = pdf_w - (2 *  padding)
avail_h = pdf_h - (2 * padding)

image_w, image_h = image.size
aspect_ratio = image_h / image_w

# calculate how to scale the image
scale_w = avail_w / image_w
scale_h = avail_h / image_h 
scale = min(scale_w, scale_h)

# apply scaling to the image width and height to get the new width and height
img_w = image_w * scale
img_h = image_h * scale

# calculate position to ccenter image with padded region
x = padding + (avail_w - img_w) * 0.5
y = padding + (avail_h - img_h) * 0.5

# put image into pdf
pdf.image(image_path, x, y, img_w, img_h)

# what to name it and where to save it
pdf_output_path = './TTT_GUI_AI_UML.pdf'

# save it
pdf.output(pdf_output_path)

pdf_output_path